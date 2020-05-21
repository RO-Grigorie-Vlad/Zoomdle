import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Route = {
  path: 'settings',
  component: SettingsComponent,
  data: {
    // authorities: ['ROLE_USER'],
    authorities: ['ROLE_USER', 'ROLE_STUDENT', 'ROLE_PROFESOR', 'ROLE_ADMIN'],
    pageTitle: 'global.menu.account.settings'
  },
  canActivate: [UserRouteAccessService]
};
