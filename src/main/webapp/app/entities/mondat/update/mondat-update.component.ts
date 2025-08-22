import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IMondat } from '../mondat.model';
import { MondatService } from '../service/mondat.service';
import { MondatFormGroup, MondatFormService } from './mondat-form.service';

@Component({
  selector: 'jhi-mondat-update',
  templateUrl: './mondat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MondatUpdateComponent implements OnInit {
  isSaving = false;
  mondat: IMondat | null = null;

  comptesSharedCollection: ICompte[] = [];

  protected mondatService = inject(MondatService);
  protected mondatFormService = inject(MondatFormService);
  protected compteService = inject(CompteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MondatFormGroup = this.mondatFormService.createMondatFormGroup();

  compareCompte = (o1: ICompte | null, o2: ICompte | null): boolean => this.compteService.compareCompte(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mondat }) => {
      this.mondat = mondat;
      if (mondat) {
        this.updateForm(mondat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mondat = this.mondatFormService.getMondat(this.editForm);
    if (mondat.id !== null) {
      this.subscribeToSaveResponse(this.mondatService.update(mondat));
    } else {
      this.subscribeToSaveResponse(this.mondatService.create(mondat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMondat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mondat: IMondat): void {
    this.mondat = mondat;
    this.mondatFormService.resetForm(this.editForm, mondat);

    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing<ICompte>(this.comptesSharedCollection, mondat.compte);
  }

  protected loadRelationshipsOptions(): void {
    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing<ICompte>(comptes, this.mondat?.compte)))
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }
}
