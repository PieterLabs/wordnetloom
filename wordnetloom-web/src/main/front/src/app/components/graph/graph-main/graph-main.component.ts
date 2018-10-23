import {AfterViewInit, Component, ElementRef, HostListener, Inject, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {GraphService} from '../graph.service';
import { ElementQueries, ResizeSensor } from 'css-element-queries' ;

// declare const GraphCreator: any;

@Component({
  selector: 'app-graph-main',
  templateUrl: './graph-main.component.html',
  styleUrls: ['./graph-main.component.css']
})
export class GraphMainComponent implements OnInit, AfterViewInit {

  graph: any;
  @ViewChild('graphContainerDiv') graphContainerDiv: ElementRef;
  resizeTimeout: number;
  lastClickedNodeIdSubscription: Subscription;
  breakPoint = 768;


  constructor(@Inject('GraphCreator') public graphCreator: any, private graphService: GraphService) { }

  private getSpaceForGraph() {
    const breakPoint = this.breakPoint;
    if (window.innerWidth < breakPoint) {
      return [ window.innerWidth - 110, window.innerHeight - 130];
    } else {
      return [(window.innerWidth - 55) / 12 * 8 - 38, window.innerHeight - 130];
    }
  }

  ngOnInit() {
    const graphSpace = this.getSpaceForGraph();
    const height = this.graphContainerDiv.nativeElement.offsetHeight,
      width = this.graphContainerDiv.nativeElement.offsetWidth,
      showSearchBox = false;

    this.graph = new this.graphCreator.GraphCreator('graph-container', showSearchBox, width, height);

    if (window.innerWidth >= this.breakPoint) {
      //                  scale, parent, top, right, bottom, left
      this.graph.showMiniMap(.25, null, '5px', null, null, '5px');
    }
    this.graphService.initService(this.graph);
    this.graphService.initializeFromSynsetId(10);

    this.graphContainerDiv.nativeElement.onresize = this.graphContainerResized;
  }

  ngAfterViewInit() {
    ElementQueries.listen();
    ElementQueries.init();

    const resizeSensor = new ResizeSensor(this.graphContainerDiv.nativeElement, this.graphContainerResized.bind(this));
  }

  onDestroy() {
    this.graphService.destroy();
    this.lastClickedNodeIdSubscription.unsubscribe();
  }

  graphContainerResized(event) {
    console.log(event);
    if (this.resizeTimeout) {
      clearTimeout(this.resizeTimeout);
    }
    this.resizeTimeout = setTimeout((() => {
      const width = event.width,
        height = event.height;
      this.graph.resizeSVG(width, height);
    }).bind(this), 250);
  }

}