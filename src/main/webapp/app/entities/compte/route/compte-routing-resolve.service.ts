import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompte } from '../compte.model';
import { CompteService } from '../service/compte.service';

const compteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompte> => {
  const id = route.params.id;
  if (id) {
    return inject(CompteService)
      .find(id)
      .pipe(
        mergeMap((compte: HttpResponse<ICompte>) => {
          if (compte.body) {
            return of(compte.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default compteResolve;
