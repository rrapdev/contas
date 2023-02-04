import dayjs from 'dayjs/esm';

import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

import { IContaPagamento, NewContaPagamento } from './conta-pagamento.model';

export const sampleWithRequiredData: IContaPagamento = {
  id: 77559,
  dataVencimento: dayjs('2023-02-04'),
};

export const sampleWithPartialData: IContaPagamento = {
  id: 70741,
  dataVencimento: dayjs('2023-02-04'),
  valor: 6872,
  dataPagamento: dayjs('2023-02-04'),
};

export const sampleWithFullData: IContaPagamento = {
  id: 39913,
  dataVencimento: dayjs('2023-02-04'),
  descricao: 'granular',
  valor: 94658,
  dataPagamento: dayjs('2023-02-04'),
  valorPago: 66867,
  observacoes: 'Account payment',
  periodicidade: Periodicidade['MENSAL'],
};

export const sampleWithNewData: NewContaPagamento = {
  dataVencimento: dayjs('2023-02-04'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
