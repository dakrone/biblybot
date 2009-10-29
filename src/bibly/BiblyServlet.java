package bibly;

import com.google.wave.api.*;

//@SuppressWarnings("serial")
//public class BiblyServlet extends HttpServlet {
//      public void doGet(HttpServletRequest req, HttpServletResponse resp)
//                  throws IOException {
//            resp.setContentType("text/plain");
//            resp.getWriter().println("Hello, world");
//      }
//}

@SuppressWarnings("serial")
public class BiblyServlet extends AbstractRobotServlet {

      @Override
      public void processEvents(RobotMessageBundle bundle) {
        Wavelet wavelet = bundle.getWavelet();
                  
        if (bundle.wasSelfAdded()) {
          Blip blip = wavelet.appendBlip();
          TextView textView = blip.getDocument();
          textView.append("I'm alive!");
        }
                
        for (Event e: bundle.getEvents()) {
          if (e.getType() == EventType.WAVELET_PARTICIPANTS_CHANGED) {    
            Blip blip = wavelet.appendBlip();
            TextView textView = blip.getDocument();
            textView.append("Hi, everybody!");
          }
        }
      }
    }