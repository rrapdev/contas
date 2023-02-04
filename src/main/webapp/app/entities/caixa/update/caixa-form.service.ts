import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICaixa, NewCaixa } from '../caixa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICaixa for edit and NewCaixaFormGroupInput for create.
 */
type CaixaFormGroupInput = ICaixa | PartialWithRequiredKeyOf<NewCaixa>;

type CaixaFormDefaults = Pick<NewCaixa, 'id'>;

type CaixaFormGroupContent = {
  id: FormControl<ICaixa['id'] | NewCaixa['id']>;
  nomeCaixa: FormControl<ICaixa['nomeCaixa']>;
};

export type CaixaFormGroup = FormGroup<CaixaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CaixaFormService {
  createCaixaFormGroup(caixa: CaixaFormGroupInput = { id: null }): CaixaFormGroup {
    const caixaRawValue = {
      ...this.getFormDefaults(),
      ...caixa,
    };
    return new FormGroup<CaixaFormGroupContent>({
      id: new FormControl(
        { value: caixaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomeCaixa: new FormControl(caixaRawValue.nomeCaixa),
    });
  }

  getCaixa(form: CaixaFormGroup): ICaixa | NewCaixa {
    return form.getRawValue() as ICaixa | NewCaixa;
  }

  resetForm(form: CaixaFormGroup, caixa: CaixaFormGroupInput): void {
    const caixaRawValue = { ...this.getFormDefaults(), ...caixa };
    form.reset(
      {
        ...caixaRawValue,
        id: { value: caixaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CaixaFormDefaults {
    return {
      id: null,
    };
  }
}
