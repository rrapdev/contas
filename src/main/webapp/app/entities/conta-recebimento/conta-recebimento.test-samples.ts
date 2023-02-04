import dayjs from 'dayjs/esm';

import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

import { IContaRecebimento, NewContaRecebimento } from './conta-recebimento.model';

export const sampleWithRequiredData: IContaRecebimento = {
  id: 33122,
  dataVencimento: dayjs('2023-02-04'),
};

export const sampleWithPartialData: IContaRecebimento = {
  id: 44550,
  dataVencimento: dayjs('2023-02-04'),
};

export const sampleWithFullData: IContaRecebimento = {
  id: 26035,
  dataVencimento: dayjs('2023-02-04'),
  descricao: 'Norte Sapatos wireless',
  valor: 29993,
  dataRecebimento: dayjs('2023-02-04'),
  valorRecebido: 42601,
  observacoes: 'Borracha',
  periodicidade: Periodicidade['SEMANAL'],
};

export const sampleWithNewData: NewContaRecebimento = {
  dataVencimento: dayjs('2023-02-04'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
