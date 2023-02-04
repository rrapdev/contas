import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaRecebimento, NewCategoriaRecebimento } from '../categoria-recebimento.model';

export type PartialUpdateCategoriaRecebimento = Partial<ICategoriaRecebimento> & Pick<ICategoriaRecebimento, 'id'>;

export type EntityResponseType = HttpResponse<ICategoriaRecebimento>;
export type EntityArrayResponseType = HttpResponse<ICategoriaRecebimento[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaRecebimentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-recebimentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaRecebimento: NewCategoriaRecebimento): Observable<EntityResponseType> {
    return this.http.post<ICategoriaRecebimento>(this.resourceUrl, categoriaRecebimento, { observe: 'response' });
  }

  update(categoriaRecebimento: ICategoriaRecebimento): Observable<EntityResponseType> {
    return this.http.put<ICategoriaRecebimento>(
      `${this.resourceUrl}/${this.getCategoriaRecebimentoIdentifier(categoriaRecebimento)}`,
      categoriaRecebimento,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaRecebimento: PartialUpdateCategoriaRecebimento): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaRecebimento>(
      `${this.resourceUrl}/${this.getCategoriaRecebimentoIdentifier(categoriaRecebimento)}`,
      categoriaRecebimento,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaRecebimento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaRecebimento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategoriaRecebimentoIdentifier(categoriaRecebimento: Pick<ICategoriaRecebimento, 'id'>): number {
    return categoriaRecebimento.id;
  }

  compareCategoriaRecebimento(o1: Pick<ICategoriaRecebimento, 'id'> | null, o2: Pick<ICategoriaRecebimento, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoriaRecebimentoIdentifier(o1) === this.getCategoriaRecebimentoIdentifier(o2) : o1 === o2;
  }

  addCategoriaRecebimentoToCollectionIfMissing<Type extends Pick<ICategoriaRecebimento, 'id'>>(
    categoriaRecebimentoCollection: Type[],
    ...categoriaRecebimentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categoriaRecebimentos: Type[] = categoriaRecebimentosToCheck.filter(isPresent);
    if (categoriaRecebimentos.length > 0) {
      const categoriaRecebimentoCollectionIdentifiers = categoriaRecebimentoCollection.map(
        categoriaRecebimentoItem => this.getCategoriaRecebimentoIdentifier(categoriaRecebimentoItem)!
      );
      const categoriaRecebimentosToAdd = categoriaRecebimentos.filter(categoriaRecebimentoItem => {
        const categoriaRecebimentoIdentifier = this.getCategoriaRecebimentoIdentifier(categoriaRecebimentoItem);
        if (categoriaRecebimentoCollectionIdentifiers.includes(categoriaRecebimentoIdentifier)) {
          return false;
        }
        categoriaRecebimentoCollectionIdentifiers.push(categoriaRecebimentoIdentifier);
        return true;
      });
      return [...categoriaRecebimentosToAdd, ...categoriaRecebimentoCollection];
    }
    return categoriaRecebimentoCollection;
  }
}
