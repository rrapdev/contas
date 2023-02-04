import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaPagamento, NewCategoriaPagamento } from '../categoria-pagamento.model';

export type PartialUpdateCategoriaPagamento = Partial<ICategoriaPagamento> & Pick<ICategoriaPagamento, 'id'>;

export type EntityResponseType = HttpResponse<ICategoriaPagamento>;
export type EntityArrayResponseType = HttpResponse<ICategoriaPagamento[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaPagamentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-pagamentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaPagamento: NewCategoriaPagamento): Observable<EntityResponseType> {
    return this.http.post<ICategoriaPagamento>(this.resourceUrl, categoriaPagamento, { observe: 'response' });
  }

  update(categoriaPagamento: ICategoriaPagamento): Observable<EntityResponseType> {
    return this.http.put<ICategoriaPagamento>(
      `${this.resourceUrl}/${this.getCategoriaPagamentoIdentifier(categoriaPagamento)}`,
      categoriaPagamento,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaPagamento: PartialUpdateCategoriaPagamento): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaPagamento>(
      `${this.resourceUrl}/${this.getCategoriaPagamentoIdentifier(categoriaPagamento)}`,
      categoriaPagamento,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaPagamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaPagamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriaPagamentoIdentifier(categoriaPagamento: Pick<ICategoriaPagamento, 'id'>): number {
    return categoriaPagamento.id;
  }

  compareCategoriaPagamento(o1: Pick<ICategoriaPagamento, 'id'> | null, o2: Pick<ICategoriaPagamento, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaPagamentoIdentifier(o1) === this.getCategoriaPagamentoIdentifier(o2) : o1 === o2;
  }

  addCategoriaPagamentoToCollectionIfMissing<Type extends Pick<ICategoriaPagamento, 'id'>>(
    categoriaPagamentoCollection: Type[],
    ...categoriaPagamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categoriaPagamentos: Type[] = categoriaPagamentosToCheck.filter(isPresent);
    if (categoriaPagamentos.length > 0) {
      const categoriaPagamentoCollectionIdentifiers = categoriaPagamentoCollection.map(
        categoriaPagamentoItem => this.getCategoriaPagamentoIdentifier(categoriaPagamentoItem)!
      );
      const categoriaPagamentosToAdd = categoriaPagamentos.filter(categoriaPagamentoItem => {
        const categoriaPagamentoIdentifier = this.getCategoriaPagamentoIdentifier(categoriaPagamentoItem);
        if (categoriaPagamentoCollectionIdentifiers.includes(categoriaPagamentoIdentifier)) {
          return false;
        }
        categoriaPagamentoCollectionIdentifiers.push(categoriaPagamentoIdentifier);
        return true;
      });
      return [...categoriaPagamentosToAdd, ...categoriaPagamentoCollection];
    }
    return categoriaPagamentoCollection;
  }
}
