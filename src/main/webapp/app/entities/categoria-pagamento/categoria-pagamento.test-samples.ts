import { ICategoriaPagamento, NewCategoriaPagamento } from './categoria-pagamento.model';

export const sampleWithRequiredData: ICategoriaPagamento = {
  id: 7367,
};

export const sampleWithPartialData: ICategoriaPagamento = {
  id: 12020,
  nomeCategoria: 'Acre',
};

export const sampleWithFullData: ICategoriaPagamento = {
  id: 53591,
  nomeCategoria: 'human-resource redundant',
};

export const sampleWithNewData: NewCategoriaPagamento = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
