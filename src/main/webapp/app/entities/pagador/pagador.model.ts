export interface IPagador {
  id: number;
  nomePagador?: string | null;
  cpfCnpj?: string | null;
}

export type NewPagador = Omit<IPagador, 'id'> & { id: null };
