import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IOperation } from '../operation.model';
import { OperationService } from '../service/operation.service';
import { OperationFormGroup, OperationFormService } from './operation-form.service';

@Component({
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;
  operation: IOperation | null = null;

  comptesSharedCollection: ICompte[] = [];

  protected operationService = inject(OperationService);
  protected operationFormService = inject(OperationFormService);
  protected compteService = inject(CompteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OperationFormGroup = this.operationFormService.createOperationFormGroup();

  compareCompte = (o1: ICompte | null, o2: ICompte | null): boolean => this.compteService.compareCompte(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      this.operation = operation;
      if (operation) {
        this.updateForm(operation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.operationFormService.getOperation(this.editForm);
    if (operation.id !== null) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.operation = operation;
    this.operationFormService.resetForm(this.editForm, operation);

    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing<ICompte>(
      this.comptesSharedCollection,
      operation.compte,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing<ICompte>(comptes, this.operation?.compte)))
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }
}
