import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { IVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';
import { VirementFormGroup, VirementFormService } from './virement-form.service';

@Component({
  selector: 'jhi-virement-update',
  templateUrl: './virement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VirementUpdateComponent implements OnInit {
  isSaving = false;
  virement: IVirement | null = null;

  comptesSharedCollection: ICompte[] = [];

  protected virementService = inject(VirementService);
  protected virementFormService = inject(VirementFormService);
  protected compteService = inject(CompteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VirementFormGroup = this.virementFormService.createVirementFormGroup();

  compareCompte = (o1: ICompte | null, o2: ICompte | null): boolean => this.compteService.compareCompte(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ virement }) => {
      this.virement = virement;
      if (virement) {
        this.updateForm(virement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const virement = this.virementFormService.getVirement(this.editForm);
    if (virement.id !== null) {
      this.subscribeToSaveResponse(this.virementService.update(virement));
    } else {
      this.subscribeToSaveResponse(this.virementService.create(virement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVirement>>): void {
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

  protected updateForm(virement: IVirement): void {
    this.virement = virement;
    this.virementFormService.resetForm(this.editForm, virement);

    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing<ICompte>(
      this.comptesSharedCollection,
      virement.compte,
      virement.beneficiaire,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(
        map((comptes: ICompte[]) =>
          this.compteService.addCompteToCollectionIfMissing<ICompte>(comptes, this.virement?.compte, this.virement?.beneficiaire),
        ),
      )
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }
}
