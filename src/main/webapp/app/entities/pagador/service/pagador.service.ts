import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPagador, NewPagador } from '../pagador.model';

export type PartialUpdatePagador = Partial<IPagador> & Pick<IPagador, 'id'>;

export type EntityResponseType = HttpResponse<IPagador>;
export type EntityArrayResponseType = HttpResponse<IPagador[]>;

@Injectable({ providedIn: 'root' })
export class PagadorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pagadors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pagador: NewPagador): Observable<EntityResponseType> {
    return this.http.post<IPagador>(this.resourceUrl, pagador, { observe: 'response' });
  }

  update(pagador: IPagador): Observable<EntityResponseType> {
    return this.http.put<IPagador>(`${this.resourceUrl}/${this.getPagadorIdentifier(pagador)}`, pagador, { observe: 'response' });
  }

  partialUpdate(pagador: PartialUpdatePagador): Observable<EntityResponseType> {
    return this.http.patch<IPagador>(`${this.resourceUrl}/${this.getPagadorIdentifier(pagador)}`, pagador, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPagador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPagador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPagadorIdentifier(pagador: Pick<IPagador, 'id'>): number {
    return pagador.id;
  }

  comparePagador(o1: Pick<IPagador, 'id'> | null, o2: Pick<IPagador, 'id'> | null): boolean {
    return o1 && o2 ? this.getPagadorIdentifier(o1) === this.getPagadorIdentifier(o2) : o1 === o2;
  }

  addPagadorToCollectionIfMissing<Type extends Pick<IPagador, 'id'>>(
    pagadorCollection: Type[],
    ...pagadorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pagadors: Type[] = pagadorsToCheck.filter(isPresent);
    if (pagadors.length > 0) {
      const pagadorCollectionIdentifiers = pagadorCollection.map(pagadorItem => this.getPagadorIdentifier(pagadorItem)!);
      const pagadorsToAdd = pagadors.filter(pagadorItem => {
        const pagadorIdentifier = this.getPagadorIdentifier(pagadorItem);
        if (pagadorCollectionIdentifiers.includes(pagadorIdentifier)) {
          return false;
        }
        pagadorCollectionIdentifiers.push(pagadorIdentifier);
        return true;
      });
      return [...pagadorsToAdd, ...pagadorCollection];
    }
    return pagadorCollection;
  }
}
