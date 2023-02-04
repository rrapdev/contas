import dayjs from 'dayjs/esm';
import { IPagador } from 'app/entities/pagador/pagador.model';
import { ICategoriaRecebimento } from 'app/entities/categoria-recebimento/categoria-recebimento.model';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

export interface IContaRecebimento {
  id: number;
  dataVencimento?: dayjs.Dayjs | null;
  descricao?: string | null;
  valor?: number | null;
  dataRecebimento?: dayjs.Dayjs | null;
  valorRecebido?: number | null;
  observacoes?: string | null;
  periodicidade?: Periodicidade | null;
  pagador?: Pick<IPagador, 'id' | 'nomePagador'> | null;
  categoriaRecebimento?: Pick<ICategoriaRecebimento, 'id' | 'nomeCategoria'> | null;
  caixa?: Pick<ICaixa, 'id' | 'nomeCaixa'> | null;
}

export type NewContaRecebimento = Omit<IContaRecebimento, 'id'> & { id: null };
