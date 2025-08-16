import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVirement, NewVirement } from '../virement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVirement for edit and NewVirementFormGroupInput for create.
 */
type VirementFormGroupInput = IVirement | PartialWithRequiredKeyOf<NewVirement>;

type VirementFormDefaults = Pick<NewVirement, 'id'>;

type VirementFormGroupContent = {
  id: FormControl<IVirement['id'] | NewVirement['id']>;
  compteBeneficiaire: FormControl<IVirement['compteBeneficiaire']>;
  motif: FormControl<IVirement['motif']>;
  compte: FormControl<IVirement['compte']>;
  beneficiaire: FormControl<IVirement['beneficiaire']>;
};

export type VirementFormGroup = FormGroup<VirementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VirementFormService {
  createVirementFormGroup(virement: VirementFormGroupInput = { id: null }): VirementFormGroup {
    const virementRawValue = {
      ...this.getFormDefaults(),
      ...virement,
    };
    return new FormGroup<VirementFormGroupContent>({
      id: new FormControl(
        { value: virementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      compteBeneficiaire: new FormControl(virementRawValue.compteBeneficiaire, {
        validators: [Validators.required],
      }),
      motif: new FormControl(virementRawValue.motif, {
        validators: [Validators.required],
      }),
      compte: new FormControl(virementRawValue.compte),
      beneficiaire: new FormControl(virementRawValue.beneficiaire),
    });
  }

  getVirement(form: VirementFormGroup): IVirement | NewVirement {
    return form.getRawValue() as IVirement | NewVirement;
  }

  resetForm(form: VirementFormGroup, virement: VirementFormGroupInput): void {
    const virementRawValue = { ...this.getFormDefaults(), ...virement };
    form.reset(
      {
        ...virementRawValue,
        id: { value: virementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VirementFormDefaults {
    return {
      id: null,
    };
  }
}
