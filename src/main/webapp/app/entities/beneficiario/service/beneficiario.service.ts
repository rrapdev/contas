import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeneficiario, NewBeneficiario } from '../beneficiario.model';

export type PartialUpdateBeneficiario = Partial<IBeneficiario> & Pick<IBeneficiario, 'id'>;

export type EntityResponseType = HttpResponse<IBeneficiario>;
export type EntityArrayResponseType = HttpResponse<IBeneficiario[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beneficiario: NewBeneficiario): Observable<EntityResponseType> {
    return this.http.post<IBeneficiario>(this.resourceUrl, beneficiario, { observe: 'response' });
  }

  update(beneficiario: IBeneficiario): Observable<EntityResponseType> {
    return this.http.put<IBeneficiario>(`${this.resourceUrl}/${this.getBeneficiarioIdentifier(beneficiario)}`, beneficiario, {
      observe: 'response',
    });
  }

  partialUpdate(beneficiario: PartialUpdateBeneficiario): Observable<EntityResponseType> {
    return this.http.patch<IBeneficiario>(`${this.resourceUrl}/${this.getBeneficiarioIdentifier(beneficiario)}`, beneficiario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeneficiario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeneficiario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeneficiarioIdentifier(beneficiario: Pick<IBeneficiario, 'id'>): number {
    return beneficiario.id;
  }

  compareBeneficiario(o1: Pick<IBeneficiario, 'id'> | null, o2: Pick<IBeneficiario, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeneficiarioIdentifier(o1) === this.getBeneficiarioIdentifier(o2) : o1 === o2;
  }

  addBeneficiarioToCollectionIfMissing<Type extends Pick<IBeneficiario, 'id'>>(
    beneficiarioCollection: Type[],
    ...beneficiariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beneficiarios: Type[] = beneficiariosToCheck.filter(isPresent);
    if (beneficiarios.length > 0) {
      const beneficiarioCollectionIdentifiers = beneficiarioCollection.map(
        beneficiarioItem => this.getBeneficiarioIdentifier(beneficiarioItem)!
      );
      const beneficiariosToAdd = beneficiarios.filter(beneficiarioItem => {
        const beneficiarioIdentifier = this.getBeneficiarioIdentifier(beneficiarioItem);
        if (beneficiarioCollectionIdentifiers.includes(beneficiarioIdentifier)) {
          return false;
        }
        beneficiarioCollectionIdentifiers.push(beneficiarioIdentifier);
        return true;
      });
      return [...beneficiariosToAdd, ...beneficiarioCollection];
    }
    return beneficiarioCollection;
  }
}
