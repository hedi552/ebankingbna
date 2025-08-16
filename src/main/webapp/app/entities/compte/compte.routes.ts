import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CompteResolve from './route/compte-routing-resolve.service';

const compteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compte.component').then(m => m.CompteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compte-detail.component').then(m => m.CompteDetailComponent),
    resolve: {
      compte: CompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compte-update.component').then(m => m.CompteUpdateComponent),
    resolve: {
      compte: CompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compte-update.component').then(m => m.CompteUpdateComponent),
    resolve: {
      compte: CompteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compteRoute;
