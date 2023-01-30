import { MapSearchResult } from "./MapSearchResult";
import { User } from "./User";

export interface FavoriteRouteDto{
    locations: MapSearchResult[],
    clientEmail: String
}