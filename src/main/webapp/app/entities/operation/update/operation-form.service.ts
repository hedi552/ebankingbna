import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOperation, NewOperation } from '../operation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperation for edit and NewOperationFormGroupInput for create.
 */
type OperationFormGroupInput = IOperation | PartialWithRequiredKeyOf<NewOperation>;

type OperationFormDefaults = Pick<NewOperation, 'id'>;

type OperationFormGroupContent = {
  id: FormControl<IOperation['id'] | NewOperation['id']>;
  montant: FormControl<IOperation['montant']>;
  libelle: FormControl<IOperation['libelle']>;
  compte: FormControl<IOperation['compte']>;
};

export type OperationFormGroup = FormGroup<OperationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperationFormService {
  createOperationFormGroup(operation: OperationFormGroupInput = { id: null }): OperationFormGroup {
    const operationRawValue = {
      ...this.getFormDefaults(),
      ...operation,
    };
    return new FormGroup<OperationFormGroupContent>({
      id: new FormControl(
        { value: operationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      montant: new FormControl(operationRawValue.montant, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(operationRawValue.libelle, {
        validators: [Validators.required],
      }),
      compte: new FormControl(operationRawValue.compte),
    });
  }

  getOperation(form: OperationFormGroup): IOperation | NewOperation {
    return form.getRawValue() as IOperation | NewOperation;
  }

  resetForm(form: OperationFormGroup, operation: OperationFormGroupInput): void {
    const operationRawValue = { ...this.getFormDefaults(), ...operation };
    form.reset(
      {
        ...operationRawValue,
        id: { value: operationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OperationFormDefaults {
    return {
      id: null,
    };
  }
}
