import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContaRecebimento, NewContaRecebimento } from '../conta-recebimento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContaRecebimento for edit and NewContaRecebimentoFormGroupInput for create.
 */
type ContaRecebimentoFormGroupInput = IContaRecebimento | PartialWithRequiredKeyOf<NewContaRecebimento>;

type ContaRecebimentoFormDefaults = Pick<NewContaRecebimento, 'id'>;

type ContaRecebimentoFormGroupContent = {
  id: FormControl<IContaRecebimento['id'] | NewContaRecebimento['id']>;
  dataVencimento: FormControl<IContaRecebimento['dataVencimento']>;
  descricao: FormControl<IContaRecebimento['descricao']>;
  valor: FormControl<IContaRecebimento['valor']>;
  dataRecebimento: FormControl<IContaRecebimento['dataRecebimento']>;
  valorRecebido: FormControl<IContaRecebimento['valorRecebido']>;
  observacoes: FormControl<IContaRecebimento['observacoes']>;
  periodicidade: FormControl<IContaRecebimento['periodicidade']>;
  pagador: FormControl<IContaRecebimento['pagador']>;
  categoriaRecebimento: FormControl<IContaRecebimento['categoriaRecebimento']>;
  caixa: FormControl<IContaRecebimento['caixa']>;
};

export type ContaRecebimentoFormGroup = FormGroup<ContaRecebimentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContaRecebimentoFormService {
  createContaRecebimentoFormGroup(contaRecebimento: ContaRecebimentoFormGroupInput = { id: null }): ContaRecebimentoFormGroup {
    const contaRecebimentoRawValue = {
      ...this.getFormDefaults(),
      ...contaRecebimento,
    };
    return new FormGroup<ContaRecebimentoFormGroupContent>({
      id: new FormControl(
        { value: contaRecebimentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataVencimento: new FormControl(contaRecebimentoRawValue.dataVencimento, {
        validators: [Validators.required],
      }),
      descricao: new FormControl(contaRecebimentoRawValue.descricao),
      valor: new FormControl(contaRecebimentoRawValue.valor),
      dataRecebimento: new FormControl(contaRecebimentoRawValue.dataRecebimento),
      valorRecebido: new FormControl(contaRecebimentoRawValue.valorRecebido),
      observacoes: new FormControl(contaRecebimentoRawValue.observacoes),
      periodicidade: new FormControl(contaRecebimentoRawValue.periodicidade),
      pagador: new FormControl(contaRecebimentoRawValue.pagador),
      categoriaRecebimento: new FormControl(contaRecebimentoRawValue.categoriaRecebimento),
      caixa: new FormControl(contaRecebimentoRawValue.caixa),
    });
  }

  getContaRecebimento(form: ContaRecebimentoFormGroup): IContaRecebimento | NewContaRecebimento {
    return form.getRawValue() as IContaRecebimento | NewContaRecebimento;
  }

  resetForm(form: ContaRecebimentoFormGroup, contaRecebimento: ContaRecebimentoFormGroupInput): void {
    const contaRecebimentoRawValue = { ...this.getFormDefaults(), ...contaRecebimento };
    form.reset(
      {
        ...contaRecebimentoRawValue,
        id: { value: contaRecebimentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContaRecebimentoFormDefaults {
    return {
      id: null,
    };
  }
}
