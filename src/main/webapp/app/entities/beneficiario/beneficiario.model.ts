export interface IBeneficiario {
  id: number;
  nomeBeneficiario?: string | null;
  cpfCnpj?: string | null;
}

export type NewBeneficiario = Omit<IBeneficiario, 'id'> & { id: null };
