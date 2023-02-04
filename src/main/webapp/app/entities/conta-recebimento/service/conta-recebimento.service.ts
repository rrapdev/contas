import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContaRecebimento, NewContaRecebimento } from '../conta-recebimento.model';

export type PartialUpdateContaRecebimento = Partial<IContaRecebimento> & Pick<IContaRecebimento, 'id'>;

type RestOf<T extends IContaRecebimento | NewContaRecebimento> = Omit<T, 'dataVencimento' | 'dataRecebimento'> & {
  dataVencimento?: string | null;
  dataRecebimento?: string | null;
};

export type RestContaRecebimento = RestOf<IContaRecebimento>;

export type NewRestContaRecebimento = RestOf<NewContaRecebimento>;

export type PartialUpdateRestContaRecebimento = RestOf<PartialUpdateContaRecebimento>;

export type EntityResponseType = HttpResponse<IContaRecebimento>;
export type EntityArrayResponseType = HttpResponse<IContaRecebimento[]>;

@Injectable({ providedIn: 'root' })
export class ContaRecebimentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conta-recebimentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contaRecebimento: NewContaRecebimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaRecebimento);
    return this.http
      .post<RestContaRecebimento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contaRecebimento: IContaRecebimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaRecebimento);
    return this.http
      .put<RestContaRecebimento>(`${this.resourceUrl}/${this.getContaRecebimentoIdentifier(contaRecebimento)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contaRecebimento: PartialUpdateContaRecebimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaRecebimento);
    return this.http
      .patch<RestContaRecebimento>(`${this.resourceUrl}/${this.getContaRecebimentoIdentifier(contaRecebimento)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContaRecebimento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContaRecebimento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContaRecebimentoIdentifier(contaRecebimento: Pick<IContaRecebimento, 'id'>): number {
    return contaRecebimento.id;
  }

  compareContaRecebimento(o1: Pick<IContaRecebimento, 'id'> | null, o2: Pick<IContaRecebimento, 'id'> | null): boolean {
    return o1 && o2 ? this.getContaRecebimentoIdentifier(o1) === this.getContaRecebimentoIdentifier(o2) : o1 === o2;
  }

  addContaRecebimentoToCollectionIfMissing<Type extends Pick<IContaRecebimento, 'id'>>(
    contaRecebimentoCollection: Type[],
    ...contaRecebimentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contaRecebimentos: Type[] = contaRecebimentosToCheck.filter(isPresent);
    if (contaRecebimentos.length > 0) {
      const contaRecebimentoCollectionIdentifiers = contaRecebimentoCollection.map(
        contaRecebimentoItem => this.getContaRecebimentoIdentifier(contaRecebimentoItem)!
      );
      const contaRecebimentosToAdd = contaRecebimentos.filter(contaRecebimentoItem => {
        const contaRecebimentoIdentifier = this.getContaRecebimentoIdentifier(contaRecebimentoItem);
        if (contaRecebimentoCollectionIdentifiers.includes(contaRecebimentoIdentifier)) {
          return false;
        }
        contaRecebimentoCollectionIdentifiers.push(contaRecebimentoIdentifier);
        return true;
      });
      return [...contaRecebimentosToAdd, ...contaRecebimentoCollection];
    }
    return contaRecebimentoCollection;
  }

  protected convertDateFromClient<T extends IContaRecebimento | NewContaRecebimento | PartialUpdateContaRecebimento>(
    contaRecebimento: T
  ): RestOf<T> {
    return {
      ...contaRecebimento,
      dataVencimento: contaRecebimento.dataVencimento?.format(DATE_FORMAT) ?? null,
      dataRecebimento: contaRecebimento.dataRecebimento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restContaRecebimento: RestContaRecebimento): IContaRecebimento {
    return {
      ...restContaRecebimento,
      dataVencimento: restContaRecebimento.dataVencimento ? dayjs(restContaRecebimento.dataVencimento) : undefined,
      dataRecebimento: restContaRecebimento.dataRecebimento ? dayjs(restContaRecebimento.dataRecebimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContaRecebimento>): HttpResponse<IContaRecebimento> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContaRecebimento[]>): HttpResponse<IContaRecebimento[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
