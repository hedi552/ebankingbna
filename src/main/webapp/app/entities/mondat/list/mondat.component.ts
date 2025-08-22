import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IMondat } from '../mondat.model';
import { EntityArrayResponseType, MondatService } from '../service/mondat.service';
import { MondatDeleteDialogComponent } from '../delete/mondat-delete-dialog.component';

@Component({
  selector: 'jhi-mondat',
  templateUrl: './mondat.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective],
})
export class MondatComponent implements OnInit {
  subscription: Subscription | null = null;
  mondats = signal<IMondat[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly mondatService = inject(MondatService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IMondat): number => this.mondatService.getMondatIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.mondats().length === 0) {
            this.load();
          } else {
            this.mondats.set(this.refineData(this.mondats()));
          }
        }),
      )
      .subscribe();
  }

  delete(mondat: IMondat): void {
    const modalRef = this.modalService.open(MondatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mondat = mondat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.mondats.set(this.refineData(dataFromBody));
  }

  protected refineData(data: IMondat[]): IMondat[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IMondat[] | null): IMondat[] {
    return data ?? [];
  }

  // 🔹 Modified: now uses /api/operations/current-user instead of generic query
  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    return  this.mondatService
      .getByCurrentUser()
      .pipe(tap(() => (this.isLoading = false)));
  }


  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
  
  
}
