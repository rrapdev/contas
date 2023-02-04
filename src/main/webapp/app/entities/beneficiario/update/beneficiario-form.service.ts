import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBeneficiario, NewBeneficiario } from '../beneficiario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeneficiario for edit and NewBeneficiarioFormGroupInput for create.
 */
type BeneficiarioFormGroupInput = IBeneficiario | PartialWithRequiredKeyOf<NewBeneficiario>;

type BeneficiarioFormDefaults = Pick<NewBeneficiario, 'id'>;

type BeneficiarioFormGroupContent = {
  id: FormControl<IBeneficiario['id'] | NewBeneficiario['id']>;
  nomeBeneficiario: FormControl<IBeneficiario['nomeBeneficiario']>;
  cpfCnpj: FormControl<IBeneficiario['cpfCnpj']>;
};

export type BeneficiarioFormGroup = FormGroup<BeneficiarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeneficiarioFormService {
  createBeneficiarioFormGroup(beneficiario: BeneficiarioFormGroupInput = { id: null }): BeneficiarioFormGroup {
    const beneficiarioRawValue = {
      ...this.getFormDefaults(),
      ...beneficiario,
    };
    return new FormGroup<BeneficiarioFormGroupContent>({
      id: new FormControl(
        { value: beneficiarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomeBeneficiario: new FormControl(beneficiarioRawValue.nomeBeneficiario),
      cpfCnpj: new FormControl(beneficiarioRawValue.cpfCnpj),
    });
  }

  getBeneficiario(form: BeneficiarioFormGroup): IBeneficiario | NewBeneficiario {
    return form.getRawValue() as IBeneficiario | NewBeneficiario;
  }

  resetForm(form: BeneficiarioFormGroup, beneficiario: BeneficiarioFormGroupInput): void {
    const beneficiarioRawValue = { ...this.getFormDefaults(), ...beneficiario };
    form.reset(
      {
        ...beneficiarioRawValue,
        id: { value: beneficiarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BeneficiarioFormDefaults {
    return {
      id: null,
    };
  }
}
