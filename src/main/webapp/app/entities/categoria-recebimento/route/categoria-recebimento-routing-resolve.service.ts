import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaRecebimento } from '../categoria-recebimento.model';
import { CategoriaRecebimentoService } from '../service/categoria-recebimento.service';

@Injectable({ providedIn: 'root' })
export class CategoriaRecebimentoRoutingResolveService implements Resolve<ICategoriaRecebimento | null> {
  constructor(protected service: CategoriaRecebimentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaRecebimento | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaRecebimento: HttpResponse<ICategoriaRecebimento>) => {
          if (categoriaRecebimento.body) {
            return of(categoriaRecebimento.body);
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
