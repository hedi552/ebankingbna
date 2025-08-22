import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMondat } from '../mondat.model';
import { MondatService } from '../service/mondat.service';

const mondatResolve = (route: ActivatedRouteSnapshot): Observable<null | IMondat> => {
  const id = route.params.id;
  if (id) {
    return inject(MondatService)
      .find(id)
      .pipe(
        mergeMap((mondat: HttpResponse<IMondat>) => {
          if (mondat.body) {
            return of(mondat.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default mondatResolve;
