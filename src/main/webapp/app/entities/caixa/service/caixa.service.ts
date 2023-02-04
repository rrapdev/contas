import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaixa, NewCaixa } from '../caixa.model';

export type PartialUpdateCaixa = Partial<ICaixa> & Pick<ICaixa, 'id'>;

export type EntityResponseType = HttpResponse<ICaixa>;
export type EntityArrayResponseType = HttpResponse<ICaixa[]>;

@Injectable({ providedIn: 'root' })
export class CaixaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/caixas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(caixa: NewCaixa): Observable<EntityResponseType> {
    return this.http.post<ICaixa>(this.resourceUrl, caixa, { observe: 'response' });
  }

  update(caixa: ICaixa): Observable<EntityResponseType> {
    return this.http.put<ICaixa>(`${this.resourceUrl}/${this.getCaixaIdentifier(caixa)}`, caixa, { observe: 'response' });
  }

  partialUpdate(caixa: PartialUpdateCaixa): Observable<EntityResponseType> {
    return this.http.patch<ICaixa>(`${this.resourceUrl}/${this.getCaixaIdentifier(caixa)}`, caixa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICaixa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICaixa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCaixaIdentifier(caixa: Pick<ICaixa, 'id'>): number {
    return caixa.id;
  }

  compareCaixa(o1: Pick<ICaixa, 'id'> | null, o2: Pick<ICaixa, 'id'> | null): boolean {
    return o1 && o2 ? this.getCaixaIdentifier(o1) === this.getCaixaIdentifier(o2) : o1 === o2;
  }

  addCaixaToCollectionIfMissing<Type extends Pick<ICaixa, 'id'>>(
    caixaCollection: Type[],
    ...caixasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const caixas: Type[] = caixasToCheck.filter(isPresent);
    if (caixas.length > 0) {
      const caixaCollectionIdentifiers = caixaCollection.map(caixaItem => this.getCaixaIdentifier(caixaItem)!);
      const caixasToAdd = caixas.filter(caixaItem => {
        const caixaIdentifier = this.getCaixaIdentifier(caixaItem);
        if (caixaCollectionIdentifiers.includes(caixaIdentifier)) {
          return false;
        }
        caixaCollectionIdentifiers.push(caixaIdentifier);
        return true;
      });
      return [...caixasToAdd, ...caixaCollection];
    }
    return caixaCollection;
  }
}
