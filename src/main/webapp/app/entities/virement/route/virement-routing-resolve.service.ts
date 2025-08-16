import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';

const virementResolve = (route: ActivatedRouteSnapshot): Observable<null | IVirement> => {
  const id = route.params.id;
  if (id) {
    return inject(VirementService)
      .find(id)
      .pipe(
        mergeMap((virement: HttpResponse<IVirement>) => {
          if (virement.body) {
            return of(virement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default virementResolve;
