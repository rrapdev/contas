export interface ICaixa {
  id: number;
  nomeCaixa?: string | null;
}

export type NewCaixa = Omit<ICaixa, 'id'> & { id: null };
