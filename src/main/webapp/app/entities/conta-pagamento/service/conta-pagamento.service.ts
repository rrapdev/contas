import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContaPagamento, NewContaPagamento } from '../conta-pagamento.model';

export type PartialUpdateContaPagamento = Partial<IContaPagamento> & Pick<IContaPagamento, 'id'>;

type RestOf<T extends IContaPagamento | NewContaPagamento> = Omit<T, 'dataVencimento' | 'dataPagamento'> & {
  dataVencimento?: string | null;
  dataPagamento?: string | null;
};

export type RestContaPagamento = RestOf<IContaPagamento>;

export type NewRestContaPagamento = RestOf<NewContaPagamento>;

export type PartialUpdateRestContaPagamento = RestOf<PartialUpdateContaPagamento>;

export type EntityResponseType = HttpResponse<IContaPagamento>;
export type EntityArrayResponseType = HttpResponse<IContaPagamento[]>;

@Injectable({ providedIn: 'root' })
export class ContaPagamentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conta-pagamentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contaPagamento: NewContaPagamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaPagamento);
    return this.http
      .post<RestContaPagamento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contaPagamento: IContaPagamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaPagamento);
    return this.http
      .put<RestContaPagamento>(`${this.resourceUrl}/${this.getContaPagamentoIdentifier(contaPagamento)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contaPagamento: PartialUpdateContaPagamento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaPagamento);
    return this.http
      .patch<RestContaPagamento>(`${this.resourceUrl}/${this.getContaPagamentoIdentifier(contaPagamento)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContaPagamento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContaPagamento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContaPagamentoIdentifier(contaPagamento: Pick<IContaPagamento, 'id'>): number {
    return contaPagamento.id;
  }

  compareContaPagamento(o1: Pick<IContaPagamento, 'id'> | null, o2: Pick<IContaPagamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getContaPagamentoIdentifier(o1) === this.getContaPagamentoIdentifier(o2) : o1 === o2;
  }

  addContaPagamentoToCollectionIfMissing<Type extends Pick<IContaPagamento, 'id'>>(
    contaPagamentoCollection: Type[],
    ...contaPagamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contaPagamentos: Type[] = contaPagamentosToCheck.filter(isPresent);
    if (contaPagamentos.length > 0) {
      const contaPagamentoCollectionIdentifiers = contaPagamentoCollection.map(
        contaPagamentoItem => this.getContaPagamentoIdentifier(contaPagamentoItem)!
      );
      const contaPagamentosToAdd = contaPagamentos.filter(contaPagamentoItem => {
        const contaPagamentoIdentifier = this.getContaPagamentoIdentifier(contaPagamentoItem);
        if (contaPagamentoCollectionIdentifiers.includes(contaPagamentoIdentifier)) {
          return false;
        }
        contaPagamentoCollectionIdentifiers.push(contaPagamentoIdentifier);
        return true;
      });
      return [...contaPagamentosToAdd, ...contaPagamentoCollection];
    }
    return contaPagamentoCollection;
  }

  protected convertDateFromClient<T extends IContaPagamento | NewContaPagamento | PartialUpdateContaPagamento>(
    contaPagamento: T
  ): RestOf<T> {
    return {
      ...contaPagamento,
      dataVencimento: contaPagamento.dataVencimento?.format(DATE_FORMAT) ?? null,
      dataPagamento: contaPagamento.dataPagamento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContaPagamento: RestContaPagamento): IContaPagamento {
    return {
      ...restContaPagamento,
      dataVencimento: restContaPagamento.dataVencimento ? dayjs(restContaPagamento.dataVencimento) : undefined,
      dataPagamento: restContaPagamento.dataPagamento ? dayjs(restContaPagamento.dataPagamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContaPagamento>): HttpResponse<IContaPagamento> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContaPagamento[]>): HttpResponse<IContaPagamento[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
