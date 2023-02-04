export interface ICategoriaRecebimento {
  id: number;
  nomeCategoria?: string | null;
}

export type NewCategoriaRecebimento = Omit<ICategoriaRecebimento, 'id'> & { id: null };
