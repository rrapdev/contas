import { IBeneficiario, NewBeneficiario } from './beneficiario.model';

export const sampleWithRequiredData: IBeneficiario = {
  id: 81272,
};

export const sampleWithPartialData: IBeneficiario = {
  id: 37065,
  nomeBeneficiario: 'transmit Alameda web-enabled',
  cpfCnpj: 'Brunei payment Bedfordshire',
};

export const sampleWithFullData: IBeneficiario = {
  id: 34353,
  nomeBeneficiario: 'wireless Luxemburgo Genérico',
  cpfCnpj: 'Avenida Interactions Computador',
};

export const sampleWithNewData: NewBeneficiario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
