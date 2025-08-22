import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'ebankingBnaApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'compte',
    data: { pageTitle: 'ebankingBnaApp.compte.home.title' },
    loadChildren: () => import('./compte/compte.routes'),
  },
  {
    path: 'operation',
    data: { pageTitle: 'ebankingBnaApp.operation.home.title' },
    loadChildren: () => import('./operation/operation.routes'),
  },
  {
    path: 'virement',
    data: { pageTitle: 'ebankingBnaApp.virement.home.title' },
    loadChildren: () => import('./virement/virement.routes'),
  },
  {
    path: 'mondat',
    data: { pageTitle: 'ebankingBnaApp.mondat.home.title' },
    loadChildren: () => import('./mondat/mondat.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
