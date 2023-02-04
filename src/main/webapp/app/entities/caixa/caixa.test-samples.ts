import { ICaixa, NewCaixa } from './caixa.model';

export const sampleWithRequiredData: ICaixa = {
  id: 4181,
};

export const sampleWithPartialData: ICaixa = {
  id: 94185,
  nomeCaixa: 'Mercearia Alameda',
};

export const sampleWithFullData: ICaixa = {
  id: 94215,
  nomeCaixa: 'target feed',
};

export const sampleWithNewData: NewCaixa = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
