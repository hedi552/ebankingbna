import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VirementResolve from './route/virement-routing-resolve.service';

const virementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/virement.component').then(m => m.VirementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/virement-detail.component').then(m => m.VirementDetailComponent),
    resolve: {
      virement: VirementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/virement-update.component').then(m => m.VirementUpdateComponent),
    resolve: {
      virement: VirementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/virement-update.component').then(m => m.VirementUpdateComponent),
    resolve: {
      virement: VirementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default virementRoute;
