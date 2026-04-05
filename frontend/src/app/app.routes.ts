import {Routes} from '@angular/router';

import {ArticleNew} from './page/article-new/article-new';
import {MainPage} from './page/main-page/main-page';
import {Register} from './page/register/register';
import {Signin} from './page/signin/signin';

export const routesDict = [
  {name: 'Home', route: {path: '', component: MainPage}},
  {name: 'SignIn', route: {path: 'signin', component: Signin}},
  {name: 'Register', route: {path: 'register', component: Register}},
  {name: 'New', route: {path: 'new', component: ArticleNew}}
];
export const routes: Routes = routesDict.map((elem) => elem.route);
