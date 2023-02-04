import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContaPagamento, NewContaPagamento } from '../conta-pagamento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContaPagamento for edit and NewContaPagamentoFormGroupInput for create.
 */
type ContaPagamentoFormGroupInput = IContaPagamento | PartialWithRequiredKeyOf<NewContaPagamento>;

type ContaPagamentoFormDefaults = Pick<NewContaPagamento, 'id'>;

type ContaPagamentoFormGroupContent = {
  id: FormControl<IContaPagamento['id'] | NewContaPagamento['id']>;
  dataVencimento: FormControl<IContaPagamento['dataVencimento']>;
  descricao: FormControl<IContaPagamento['descricao']>;
  valor: FormControl<IContaPagamento['valor']>;
  dataPagamento: FormControl<IContaPagamento['dataPagamento']>;
  valorPago: FormControl<IContaPagamento['valorPago']>;
  observacoes: FormControl<IContaPagamento['observacoes']>;
  periodicidade: FormControl<IContaPagamento['periodicidade']>;
  beneficiario: FormControl<IContaPagamento['beneficiario']>;
  categoriaPagamento: FormControl<IContaPagamento['categoriaPagamento']>;
  caixa: FormControl<IContaPagamento['caixa']>;
};

export type ContaPagamentoFormGroup = FormGroup<ContaPagamentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContaPagamentoFormService {
  createContaPagamentoFormGroup(contaPagamento: ContaPagamentoFormGroupInput = { id: null }): ContaPagamentoFormGroup {
    const contaPagamentoRawValue = {
      ...this.getFormDefaults(),
      ...contaPagamento,
    };
    return new FormGroup<ContaPagamentoFormGroupContent>({
      id: new FormControl(
        { value: contaPagamentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataVencimento: new FormControl(contaPagamentoRawValue.dataVencimento, {
        validators: [Validators.required],
      }),
      descricao: new FormControl(contaPagamentoRawValue.descricao),
      valor: new FormControl(contaPagamentoRawValue.valor),
      dataPagamento: new FormControl(contaPagamentoRawValue.dataPagamento),
      valorPago: new FormControl(contaPagamentoRawValue.valorPago),
      observacoes: new FormControl(contaPagamentoRawValue.observacoes),
      periodicidade: new FormControl(contaPagamentoRawValue.periodicidade),
      beneficiario: new FormControl(contaPagamentoRawValue.beneficiario),
      categoriaPagamento: new FormControl(contaPagamentoRawValue.categoriaPagamento),
      caixa: new FormControl(contaPagamentoRawValue.caixa),
    });
  }

  getContaPagamento(form: ContaPagamentoFormGroup): IContaPagamento | NewContaPagamento {
    return form.getRawValue() as IContaPagamento | NewContaPagamento;
  }

  resetForm(form: ContaPagamentoFormGroup, contaPagamento: ContaPagamentoFormGroupInput): void {
    const contaPagamentoRawValue = { ...this.getFormDefaults(), ...contaPagamento };
    form.reset(
      {
        ...contaPagamentoRawValue,
        id: { value: contaPagamentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContaPagamentoFormDefaults {
    return {
      id: null,
    };
  }
}
