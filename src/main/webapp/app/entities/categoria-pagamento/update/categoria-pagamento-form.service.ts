import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategoriaPagamento, NewCategoriaPagamento } from '../categoria-pagamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoriaPagamento for edit and NewCategoriaPagamentoFormGroupInput for create.
 */
type CategoriaPagamentoFormGroupInput = ICategoriaPagamento | PartialWithRequiredKeyOf<NewCategoriaPagamento>;

type CategoriaPagamentoFormDefaults = Pick<NewCategoriaPagamento, 'id'>;

type CategoriaPagamentoFormGroupContent = {
  id: FormControl<ICategoriaPagamento['id'] | NewCategoriaPagamento['id']>;
  nomeCategoria: FormControl<ICategoriaPagamento['nomeCategoria']>;
};

export type CategoriaPagamentoFormGroup = FormGroup<CategoriaPagamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaPagamentoFormService {
  createCategoriaPagamentoFormGroup(categoriaPagamento: CategoriaPagamentoFormGroupInput = { id: null }): CategoriaPagamentoFormGroup {
    const categoriaPagamentoRawValue = {
      ...this.getFormDefaults(),
      ...categoriaPagamento,
    };
    return new FormGroup<CategoriaPagamentoFormGroupContent>({
      id: new FormControl(
        { value: categoriaPagamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomeCategoria: new FormControl(categoriaPagamentoRawValue.nomeCategoria),
    });
  }

  getCategoriaPagamento(form: CategoriaPagamentoFormGroup): ICategoriaPagamento | NewCategoriaPagamento {
    return form.getRawValue() as ICategoriaPagamento | NewCategoriaPagamento;
  }

  resetForm(form: CategoriaPagamentoFormGroup, categoriaPagamento: CategoriaPagamentoFormGroupInput): void {
    const categoriaPagamentoRawValue = { ...this.getFormDefaults(), ...categoriaPagamento };
    form.reset(
      {
        ...categoriaPagamentoRawValue,
        id: { value: categoriaPagamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaPagamentoFormDefaults {
    return {
      id: null,
    };
  }
}
