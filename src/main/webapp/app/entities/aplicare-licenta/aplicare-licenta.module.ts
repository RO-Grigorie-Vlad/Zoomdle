import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LicentaSharedModule } from 'app/shared/shared.module';
import { AplicareLicentaComponent } from './aplicare-licenta.component';
import { AplicareLicentaDetailComponent } from './aplicare-licenta-detail.component';
import { AplicareLicentaUpdateComponent } from './aplicare-licenta-update.component';
import { AplicareLicentaDeleteDialogComponent } from './aplicare-licenta-delete-dialog.component';
import { aplicareLicentaRoute } from './aplicare-licenta.route';

@NgModule({
  imports: [LicentaSharedModule, RouterModule.forChild(aplicareLicentaRoute)],
  declarations: [
    AplicareLicentaComponent,
    AplicareLicentaDetailComponent,
    AplicareLicentaUpdateComponent,
    AplicareLicentaDeleteDialogComponent
  ],
  entryComponents: [AplicareLicentaDeleteDialogComponent]
})
export class LicentaAplicareLicentaModule {}
