export interface ICategoriaPagamento {
  id: number;
  nomeCategoria?: string | null;
}

export type NewCategoriaPagamento = Omit<ICategoriaPagamento, 'id'> & { id: null };
