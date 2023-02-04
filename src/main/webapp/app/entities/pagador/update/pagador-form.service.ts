import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPagador, NewPagador } from '../pagador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPagador for edit and NewPagadorFormGroupInput for create.
 */
type PagadorFormGroupInput = IPagador | PartialWithRequiredKeyOf<NewPagador>;

type PagadorFormDefaults = Pick<NewPagador, 'id'>;

type PagadorFormGroupContent = {
  id: FormControl<IPagador['id'] | NewPagador['id']>;
  nomePagador: FormControl<IPagador['nomePagador']>;
  cpfCnpj: FormControl<IPagador['cpfCnpj']>;
};

export type PagadorFormGroup = FormGroup<PagadorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PagadorFormService {
  createPagadorFormGroup(pagador: PagadorFormGroupInput = { id: null }): PagadorFormGroup {
    const pagadorRawValue = {
      ...this.getFormDefaults(),
      ...pagador,
    };
    return new FormGroup<PagadorFormGroupContent>({
      id: new FormControl(
        { value: pagadorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomePagador: new FormControl(pagadorRawValue.nomePagador),
      cpfCnpj: new FormControl(pagadorRawValue.cpfCnpj),
    });
  }

  getPagador(form: PagadorFormGroup): IPagador | NewPagador {
    return form.getRawValue() as IPagador | NewPagador;
  }

  resetForm(form: PagadorFormGroup, pagador: PagadorFormGroupInput): void {
    const pagadorRawValue = { ...this.getFormDefaults(), ...pagador };
    form.reset(
      {
        ...pagadorRawValue,
        id: { value: pagadorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PagadorFormDefaults {
    return {
      id: null,
    };
  }
}
