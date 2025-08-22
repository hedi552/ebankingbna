import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMondat, NewMondat } from '../mondat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMondat for edit and NewMondatFormGroupInput for create.
 */
type MondatFormGroupInput = IMondat | PartialWithRequiredKeyOf<NewMondat>;

type MondatFormDefaults = Pick<NewMondat, 'id'>;

type MondatFormGroupContent = {
  id: FormControl<IMondat['id'] | NewMondat['id']>;
  compteSrc: FormControl<IMondat['compteSrc']>;
  compteBenef: FormControl<IMondat['compteBenef']>;
  montant: FormControl<IMondat['montant']>;
  code: FormControl<IMondat['code']>;
  compte: FormControl<IMondat['compte']>;
};

export type MondatFormGroup = FormGroup<MondatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MondatFormService {
  createMondatFormGroup(mondat: MondatFormGroupInput = { id: null }): MondatFormGroup {
    const mondatRawValue = {
      ...this.getFormDefaults(),
      ...mondat,
    };
    return new FormGroup<MondatFormGroupContent>({
      id: new FormControl(
        { value: mondatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      compteSrc: new FormControl(mondatRawValue.compteSrc, {
        validators: [Validators.required],
      }),
      compteBenef: new FormControl(mondatRawValue.compteBenef, {
        validators: [Validators.required],
      }),
      montant: new FormControl(mondatRawValue.montant, {
        validators: [Validators.required],
      }),
      code: new FormControl(mondatRawValue.code),
      compte: new FormControl(mondatRawValue.compte),
    });
  }

  getMondat(form: MondatFormGroup): IMondat | NewMondat {
    return form.getRawValue() as IMondat | NewMondat;
  }

  resetForm(form: MondatFormGroup, mondat: MondatFormGroupInput): void {
    const mondatRawValue = { ...this.getFormDefaults(), ...mondat };
    form.reset(
      {
        ...mondatRawValue,
        id: { value: mondatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MondatFormDefaults {
    return {
      id: null,
    };
  }
}
