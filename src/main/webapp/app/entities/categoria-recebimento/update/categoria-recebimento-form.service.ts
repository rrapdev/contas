import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategoriaRecebimento, NewCategoriaRecebimento } from '../categoria-recebimento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoriaRecebimento for edit and NewCategoriaRecebimentoFormGroupInput for create.
 */
type CategoriaRecebimentoFormGroupInput = ICategoriaRecebimento | PartialWithRequiredKeyOf<NewCategoriaRecebimento>;

type CategoriaRecebimentoFormDefaults = Pick<NewCategoriaRecebimento, 'id'>;

type CategoriaRecebimentoFormGroupContent = {
  id: FormControl<ICategoriaRecebimento['id'] | NewCategoriaRecebimento['id']>;
  nomeCategoria: FormControl<ICategoriaRecebimento['nomeCategoria']>;
};

export type CategoriaRecebimentoFormGroup = FormGroup<CategoriaRecebimentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaRecebimentoFormService {
  createCategoriaRecebimentoFormGroup(
    categoriaRecebimento: CategoriaRecebimentoFormGroupInput = { id: null }
  ): CategoriaRecebimentoFormGroup {
    const categoriaRecebimentoRawValue = {
      ...this.getFormDefaults(),
      ...categoriaRecebimento,
    };
    return new FormGroup<CategoriaRecebimentoFormGroupContent>({
      id: new FormControl(
        { value: categoriaRecebimentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomeCategoria: new FormControl(categoriaRecebimentoRawValue.nomeCategoria),
    });
  }

  getCategoriaRecebimento(form: CategoriaRecebimentoFormGroup): ICategoriaRecebimento | NewCategoriaRecebimento {
    return form.getRawValue() as ICategoriaRecebimento | NewCategoriaRecebimento;
  }

  resetForm(form: CategoriaRecebimentoFormGroup, categoriaRecebimento: CategoriaRecebimentoFormGroupInput): void {
    const categoriaRecebimentoRawValue = { ...this.getFormDefaults(), ...categoriaRecebimento };
    form.reset(
      {
        ...categoriaRecebimentoRawValue,
        id: { value: categoriaRecebimentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaRecebimentoFormDefaults {
    return {
      id: null,
    };
  }
}
