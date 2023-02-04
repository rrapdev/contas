import { IPagador, NewPagador } from './pagador.model';

export const sampleWithRequiredData: IPagador = {
  id: 90945,
};

export const sampleWithPartialData: IPagador = {
  id: 81201,
  nomePagador: 'madeira bypassing enhance',
};

export const sampleWithFullData: IPagador = {
  id: 65327,
  nomePagador: 'supply-chains',
  cpfCnpj: 'Rodovia',
};

export const sampleWithNewData: NewPagador = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
