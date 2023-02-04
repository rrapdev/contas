import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPagador } from '../pagador.model';
import { PagadorService } from '../service/pagador.service';

@Injectable({ providedIn: 'root' })
export class PagadorRoutingResolveService implements Resolve<IPagador | null> {
  constructor(protected service: PagadorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPagador | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pagador: HttpResponse<IPagador>) => {
          if (pagador.body) {
            return of(pagador.body);
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
