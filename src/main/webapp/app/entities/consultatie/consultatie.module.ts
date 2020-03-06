import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { ConsultatieComponent } from './consultatie.component';
import { ConsultatieDetailComponent } from './consultatie-detail.component';
import { ConsultatieUpdateComponent } from './consultatie-update.component';
import { ConsultatieDeleteDialogComponent } from './consultatie-delete-dialog.component';
import { consultatieRoute } from './consultatie.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(consultatieRoute)],
  declarations: [ConsultatieComponent, ConsultatieDetailComponent, ConsultatieUpdateComponent, ConsultatieDeleteDialogComponent],
  entryComponents: [ConsultatieDeleteDialogComponent]
})
export class LicentaConsultatieModule {}
