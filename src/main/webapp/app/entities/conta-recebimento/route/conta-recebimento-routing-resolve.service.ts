import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContaRecebimento } from '../conta-recebimento.model';
import { ContaRecebimentoService } from '../service/conta-recebimento.service';

@Injectable({ providedIn: 'root' })
export class ContaRecebimentoRoutingResolveService implements Resolve<IContaRecebimento | null> {
  constructor(protected service: ContaRecebimentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContaRecebimento | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contaRecebimento: HttpResponse<IContaRecebimento>) => {
          if (contaRecebimento.body) {
            return of(contaRecebimento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
