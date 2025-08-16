import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompte, NewCompte } from '../compte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompte for edit and NewCompteFormGroupInput for create.
 */
type CompteFormGroupInput = ICompte | PartialWithRequiredKeyOf<NewCompte>;

type CompteFormDefaults = Pick<NewCompte, 'id'>;

type CompteFormGroupContent = {
  id: FormControl<ICompte['id'] | NewCompte['id']>;
  numcompte: FormControl<ICompte['numcompte']>;
  agence: FormControl<ICompte['agence']>;
  user: FormControl<ICompte['user']>;
};

export type CompteFormGroup = FormGroup<CompteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteFormService {
  createCompteFormGroup(compte: CompteFormGroupInput = { id: null }): CompteFormGroup {
    const compteRawValue = {
      ...this.getFormDefaults(),
      ...compte,
    };
    return new FormGroup<CompteFormGroupContent>({
      id: new FormControl(
        { value: compteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numcompte: new FormControl(compteRawValue.numcompte, {
        validators: [Validators.required],
      }),
      agence: new FormControl(compteRawValue.agence, {
        validators: [Validators.required],
      }),
      user: new FormControl(compteRawValue.user),
    });
  }

  getCompte(form: CompteFormGroup): ICompte | NewCompte {
    return form.getRawValue() as ICompte | NewCompte;
  }

  resetForm(form: CompteFormGroup, compte: CompteFormGroupInput): void {
    const compteRawValue = { ...this.getFormDefaults(), ...compte };
    form.reset(
      {
        ...compteRawValue,
        id: { value: compteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompteFormDefaults {
    return {
      id: null,
    };
  }
}
