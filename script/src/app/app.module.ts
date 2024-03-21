import { NgModule, isDevMode } from '@angular/core';
import {
  BrowserModule,
  provideClientHydration,
} from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { en_US } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './components/auth/home/home.component';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { SiteNgZorroAntdModule } from './DemoNgZorroAndModule';
import { SignupComponent } from './components/auth/signup/signup.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CarouselModule } from 'primeng/carousel';
import { TagModule } from 'primeng/tag';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { SearchComponent } from './components/public/search/search.component';
import { ToastModule } from 'primeng/toast';
import { NzPopoverModule } from 'ng-zorro-antd/popover';
import { MegaMenuModule } from 'primeng/megamenu';
import { BadgeModule } from 'primeng/badge';
import { NzModalModule } from 'ng-zorro-antd/modal';

//module prime
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { DetailComponent } from './components/public/detail/detail.component';
import { BranchDetailComponent } from './components/public/branch-detail/branch-detail.component';
import { FileUploadModule } from 'primeng/fileupload';
import { MessageService } from 'primeng/api';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { storeUserReducer } from './shared/login.reducer';
import { VerifyAccountComponent } from './components/auth/verify-account/verify-account.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { ManageJobComponent } from './components/admin/manage-job/manage-job.component';
import { PedingApproveJobComponent } from './components/admin/peding-approve-job/peding-approve-job.component';
import { RefuseApproveJobComponent } from './components/admin/refuse-approve-job/refuse-approve-job.component';
import { ApproveJobComponent } from './components/admin/approve-job/approve-job.component';
import { JobCategoryComponent } from './components/admin/job-category/job-category.component';
import { ManageAccountComponent } from './components/admin/manage-account/manage-account.component';
import { ManageAccountBlockComponent } from './components/admin/manage-account-block/manage-account-block.component';
import { MyApplicationComponent } from './components/my-application/my-application.component';
import { ViewjobAdminComponent } from './components/viewjob-admin/viewjob-admin.component';
import { ExpiredRequestComponent } from './components/admin/expired-request/expired-request.component';
import { FavouriteComponent } from './components/favourite/favourite.component';
import { ChangepasswordComponent } from './components/changepassword/changepassword.component';
import { ConfirmforgotpasswordComponent } from './components/confirmforgotpassword/confirmforgotpassword.component';
import { ChangeforgotpasswordComponent } from './components/changeforgotpassword/changeforgotpassword.component';
import { EnteremailforgotComponent } from './components/enteremailforgot/enteremailforgot.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { PolicyComponent } from './components/policy/policy.component';
import { ViewApplyAdminComponent } from './components/view-apply-admin/view-apply-admin.component';
import { ChatComponent } from './components/chat/chat.component';
import { PassComponent } from './components/admin/pass/pass.component';
import { NotpassComponent } from './components/admin/notpass/notpass.component';
registerLocaleData(en);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    NavbarComponent,
    SearchComponent,
    DetailComponent,
    BranchDetailComponent,
    ProfileComponent,
    VerifyAccountComponent,
    DashboardComponent,
    ManageJobComponent,
    PedingApproveJobComponent,
    RefuseApproveJobComponent,
    ApproveJobComponent,
    JobCategoryComponent,
    ManageAccountComponent,
    ManageAccountBlockComponent,
    MyApplicationComponent,
    ViewjobAdminComponent,
    ExpiredRequestComponent,
    FavouriteComponent,
    ChangepasswordComponent,
    ConfirmforgotpasswordComponent,
    ChangeforgotpasswordComponent,
    EnteremailforgotComponent,
    NotFoundComponent,
    AboutComponent,
    ContactComponent,
    PolicyComponent,
    ViewApplyAdminComponent,
    ChatComponent,
    PassComponent,
    NotpassComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    SiteNgZorroAntdModule,
    ButtonModule,
    CalendarModule,
    CarouselModule,
    TagModule,
    SlickCarouselModule,
    TooltipModule,
    FileUploadModule,
    NzPopoverModule,
    MegaMenuModule,
    TableModule,
    BadgeModule,
    NzModalModule,
    StoreModule.forRoot({ storeUser: storeUserReducer }),
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states
      logOnly: !isDevMode(), // Restrict extension to log-only mode
      autoPause: true, // Pauses recording actions and state changes when the extension window is not open
      trace: false, //  If set to true, will include stack trace for every dispatched action, so you can see it in trace tab jumping directly to that part of code
      traceLimit: 75, // maximum stack trace frames to be stored (in case trace option was provided as true)
      connectInZone: true, // If set to true, the connection is established within the Angular zone
    }),
  ],
  providers: [
    provideClientHydration(),
    { provide: NZ_I18N, useValue: en_US },
    MessageService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
