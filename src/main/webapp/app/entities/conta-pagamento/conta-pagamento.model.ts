import dayjs from 'dayjs/esm';
import { IBeneficiario } from 'app/entities/beneficiario/beneficiario.model';
import { ICategoriaPagamento } from 'app/entities/categoria-pagamento/categoria-pagamento.model';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

export interface IContaPagamento {
  id: number;
  dataVencimento?: dayjs.Dayjs | null;
  descricao?: string | null;
  valor?: number | null;
  dataPagamento?: dayjs.Dayjs | null;
  valorPago?: number | null;
  observacoes?: string | null;
  periodicidade?: Periodicidade | null;
  beneficiario?: Pick<IBeneficiario, 'id' | 'nomeBeneficiario'> | null;
  categoriaPagamento?: Pick<ICategoriaPagamento, 'id' | 'nomeCategoria'> | null;
  caixa?: Pick<ICaixa, 'id' | 'nomeCaixa'> | null;
}

export type NewContaPagamento = Omit<IContaPagamento, 'id'> & { id: null };
