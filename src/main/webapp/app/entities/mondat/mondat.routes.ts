import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MondatResolve from './route/mondat-routing-resolve.service';

const mondatRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mondat.component').then(m => m.MondatComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mondat-detail.component').then(m => m.MondatDetailComponent),
    resolve: {
      mondat: MondatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mondat-update.component').then(m => m.MondatUpdateComponent),
    resolve: {
      mondat: MondatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mondat-update.component').then(m => m.MondatUpdateComponent),
    resolve: {
      mondat: MondatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mondatRoute;
