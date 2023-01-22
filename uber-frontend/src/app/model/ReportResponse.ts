import { ChartData } from "./ChartData";

export interface ReportResponse{
    ridesPerDay:ChartData ;
    kmPerDay:ChartData;
    moneyPerDay:ChartData;
    adminReport:ReportResponse;
}