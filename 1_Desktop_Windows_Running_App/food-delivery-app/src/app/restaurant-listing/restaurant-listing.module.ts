import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RestaurantListingRoutingModule } from './restaurant-listing-routing.module';
//import { RetaurantListingComponent } from '../retaurant-listing/retaurant-listing.component';
import { RestaurantListingComponent } from './components/restaurant-listing.component';


@NgModule({
  declarations: [
    RestaurantListingComponent
  ],
  imports: [
    CommonModule,
    RestaurantListingRoutingModule
  ]
})
export class RestaurantListingModule { }
