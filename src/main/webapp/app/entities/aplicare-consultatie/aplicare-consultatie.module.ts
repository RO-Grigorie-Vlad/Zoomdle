import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { AplicareConsultatieComponent } from './aplicare-consultatie.component';
import { AplicareConsultatieDetailComponent } from './aplicare-consultatie-detail.component';
import { AplicareConsultatieUpdateComponent } from './aplicare-consultatie-update.component';
import { AplicareConsultatieDeleteDialogComponent } from './aplicare-consultatie-delete-dialog.component';
import { aplicareConsultatieRoute } from './aplicare-consultatie.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(aplicareConsultatieRoute)],
  declarations: [
    AplicareConsultatieComponent,
    AplicareConsultatieDetailComponent,
    AplicareConsultatieUpdateComponent,
    AplicareConsultatieDeleteDialogComponent
  ],
  entryComponents: [AplicareConsultatieDeleteDialogComponent]
})
export class LicentaAplicareConsultatieModule {}
