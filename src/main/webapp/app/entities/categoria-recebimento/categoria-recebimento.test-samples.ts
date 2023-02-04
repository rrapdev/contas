import { ICategoriaRecebimento, NewCategoriaRecebimento } from './categoria-recebimento.model';

export const sampleWithRequiredData: ICategoriaRecebimento = {
  id: 13709,
};

export const sampleWithPartialData: ICategoriaRecebimento = {
  id: 31608,
  nomeCategoria: 'azul',
};

export const sampleWithFullData: ICategoriaRecebimento = {
  id: 29349,
  nomeCategoria: 'Licenciado',
};

export const sampleWithNewData: NewCategoriaRecebimento = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
